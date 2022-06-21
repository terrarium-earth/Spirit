#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

vec3 hash33(vec3 p){

    float n = sin(dot(p, vec3(7, 157, 113)));
    return fract(vec3(2097152, 262144, 32768)*n);
}

// 3D Voronoi: Obviously, this is just a rehash of IQ's original.
//
float voronoi(vec3 p){

    vec3 b, r, g = floor(p);
    p = fract(p); // "p -= g;" works on some GPUs, but not all, for some annoying reason.

    // Maximum value: I think outliers could get as high as "3," the squared diagonal length
    // of the unit cube, with the mid point being "0.75." Is that right? Either way, for this
    // example, the maximum is set to one, which would cover a good part of the range, whilst
    // dispensing with the need to clamp the final result.
    float d = 1.;

    // I've unrolled one of the loops. GPU architecture is a mystery to me, but I'm aware
    // they're not fond of nesting, branching, etc. My laptop GPU seems to hate everything,
    // including multiple loops. If it were a person, we wouldn't hang out.
    for(int j = -1; j <= 1; j++) {
        for(int i = -1; i <= 1; i++) {

            b = vec3(i, j, -1);
            r = b - p + hash33(g+b);
            d = min(d, dot(r,r));

            b.z = 0.0;
            r = b - p + hash33(g+b);
            d = min(d, dot(r,r));

            b.z = 1.;
            r = b - p + hash33(g+b);
            d = min(d, dot(r,r));

        }
    }

    return d; // Range: [0, 1]
}

// Standard fBm function with some time dialation to give a parallax
// kind of effect. In other words, the position and time frequencies
// are changed at different rates from layer to layer.
//
float noiseLayers(in vec3 p) {
    float iTime = 3.0 * GameTime * 1200 / 2.0;
    // Normally, you'd just add a time vector to "p," and be done with
    // it. However, in this instance, time is added seperately so that
    // its frequency can be changed at a different rate. "p.z" is thrown
    // in there just to distort things a little more.
    vec3 t = vec3(0., 0., p.z + iTime*1.5);

    const int iter = 5; // Just five layers is enough.
    float tot = 0., sum = 0., amp = 1.; // Total, sum, amplitude.

    for (int i = 0; i < iter; i++) {
        tot += voronoi(p + t) * amp; // Add the layer to the total.
        p *= 2.; // Position multiplied by two.
        t *= 1.5; // Time multiplied by less than two.
        sum += amp; // Sum of amplitudes.
        amp *= .5; // Decrease successive layer amplitude, as normal.
    }

    return tot/sum; // Range: [0, 1].
}

void main() {
    float iTime = GameTime * 1200 / 2.0;
    // Screen coordinates.
    vec2 uv = (texCoord0 - vec2(32.0)*.5) / 1000.0;

    // Shifting the central position around, just a little, to simulate a
    // moving camera, albeit a pretty lame one.
    uv += vec2(sin(iTime*.5)*.25, cos(iTime*.5)*.125);

    // Constructing the unit ray.
    vec3 rd = normalize(vec3(uv.x, uv.y, 3.1415926535898/8.));

    // Rotating the ray about the XY plane, to simulate a rolling camera.
    float cs = cos(iTime*.25), si = sin(iTime*.25);
    // Apparently "r *= rM" can break in some older browsers.
    rd.xy = rd.xy*mat2(cs, -si, si, cs);

    // Passing a unit ray multiple into the Voronoi layer function, which
    // is nothing more than an fBm setup with some time dialation.
    float c = noiseLayers(rd*2.);

    // Optional: Adding a bit of random noise for a subtle dust effect.
    c = max(c + dot(hash33(rd)*2. - 1., vec3(.015)), 0.);

    // Coloring:

    c *= c*1.5;
    vec3 col = vec3(c);

    // Rough gamma correction, and done.
    vec4 finalComputedColor = vec4(sqrt(clamp(col, 0., 1.)) * vec3(0.5, .90, 0.99), 1);
    vec4 textureColor = texture(Sampler0, texCoord0);

    if(textureColor.a < 0.1) {
        discard;
    }

    fragColor = mix(finalComputedColor, textureColor, 0.25);
}