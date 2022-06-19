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

float rand(vec2 coords) {
    return fract(sin(dot(coords, vec2(56.3456f, 78.3456f)) * 5.0f) * 10000.0f);
}

float noise(vec2 coords) {
    vec2 i = floor(coords);
    vec2 f = fract(coords);

    float a = rand(i);
    float b = rand(i + vec2(1.0f, 0.0f));
    float c = rand(i + vec2(0.0f, 1.0f));
    float d = rand(i + vec2(1.0f, 1.0f));

    vec2 cubic = f * f * (3.0f - 2.0f * f);

    return mix(a, b, cubic.x) + (c - a) * cubic.y * (1.0f - cubic.x) + (d - b) * cubic.x * cubic.y;
}

float fbm(vec2 coords) {
    float value = 0.0f;
    float scale = 0.5f;

    for (int i = 0; i < 5; i++) {
        value += noise(coords) * scale;
        coords *= 4.0f;
        scale *= 0.5f;
    }

    return value;
}

float value(vec2 uv) {
    float Pixels = 1024.0;
    float dx = 10.0 * (1.0 / Pixels);
    float dy = 10.0 * (1.0 / Pixels);


    float final = 0.0f;

    vec2 uvc = uv;

    vec2 Coord = vec2(dx * floor(uvc.x / dx),
    dy * floor(uvc.y / dy));


    for (int i =0;i < 3; i++) {
        vec2 motion = vec2(fbm(Coord + GameTime * 0.2f + vec2(i)));
        final += fbm(Coord + motion + vec2(i));
    }

    return final / 3.0f;
}

void main(void) {
    vec4 finalComputedColor = vec4(mix(vec3(-0.3f), vec3(0.55, 0.92f, 0.98f) + vec3(0.6f), value(texCoord0)), 1);
    vec4 textureColor = texture(Sampler0, texCoord0);// * vec4(0.90, 0.90, 0.90, 1);
    if(textureColor.a < 0.1) {
        discard;
    }
    fragColor = mix(finalComputedColor, textureColor, 0.65);
}