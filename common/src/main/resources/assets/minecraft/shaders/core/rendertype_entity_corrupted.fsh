#version 150
#define NUM_OCTAVES 16

#moj_import <fog.glsl>

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

mat3 rotX(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(1, 0, 0, 0, c, -s, 0, s, c);
}

mat3 rotY(float a) {
    float c = cos(a);
    float s = sin(a);

    return mat3(c, 0, -s, 0, 1, 0, s, 0, c);
}

float random(vec2 pos) {
    return fract(sin(dot(pos.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 pos) {
    vec2 i = floor(pos);
    vec2 f = fract(pos);
    float a = random(i + vec2(0.0, 0.0));
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 pos) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    mat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.5));
    for (int i = 0; i < NUM_OCTAVES; i++) {
        v += a * noise(pos);
        pos = rot * pos * 2.0 + shift;
        a *= 0.5;
    }

    return v;
}

void main(void) {
    //void main void
    vec2 p = texCoord0;
    float t = 0.0, d;
    float time2 = 3.0 * GameTime * 1200 / 2.0;

    vec2 q = vec2(0.0);
    q.x = fbm(p + 0.00 * time2);
    q.y = fbm(p + vec2(1.0));
    vec2 r = vec2(0.0);
    r.x = fbm(p + 1.0 * q + vec2(1.7, 9.2) + 0.15 * time2);
    r.y = fbm(p + 1.0 * q + vec2(8.3, 2.8) + 0.126 * time2);
    float f = fbm(p + r);
    vec3 color = mix(vec3(0.101961, 1.866667, 0.319608), vec3(0.366667, 1.598039, 0.366667), clamp((f * f) * 4.0, 0.0, 1.0));

    color = mix(color, vec3(0, 0, 0.164706), clamp(length(q), 0.0, 1.0));
    color = mix(color, vec3(0.666667, 1, 1), clamp(length(r.x), 0.0, 1.0));
    color = (f *f * f + 0.6 * f * f + 0.5 * f) * color;

    vec4 finalComputedColor = vec4(color, 1);
    vec4 textureColor = texture(Sampler0, p);// * vec4(0.90, 0.90, 0.90, 1);
    if(textureColor.a < 0.1) {
        discard;
    }
    fragColor = mix(finalComputedColor, textureColor, 0.25);
}