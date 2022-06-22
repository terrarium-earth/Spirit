#version 150

#define TAU 6.0
#define MAX_ITER 5

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
// Modified from https://www.shadertoy.com/view/MdlXz8
void main(void) {
    float time = GameTime * 600 + 23.0;
    // uv should be the 0-1 uv of texture...
    vec2 uv = texCoord0;
    vec2 p = mod(uv*TAU, TAU)-250.0;
    vec2 i = vec2(p);
    float c = 1.0;
    float inten = .005;

    for (int n = 0; n < MAX_ITER; n++) {
        float t = time * (1.0 - (3.5 / float(n+1)));
        i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
        c += 1.0/length(vec2(p.x / (sin(i.x+t)/inten),p.y / (cos(i.y+t)/inten)));
    }
    c /= float(MAX_ITER);
    c = 1.17-pow(c, 1.4);
    vec3 colour = vec3(pow(abs(c), 8.0));
    colour = clamp(colour + vec3(0.35, 0.65, 0.69), 0.0, 1.0);

    vec4 finalComputedColor = vec4(colour, .55);
    vec4 textureColor = texture(Sampler0, texCoord0);

    if(textureColor.a < 0.1) {
        discard;
    }

    fragColor = mix(finalComputedColor, textureColor, 0.40);
}