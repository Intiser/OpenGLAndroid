//Ref URL : https://www.shadertoy.com/view/MsXcWl
#extension GL_OES_EGL_image_external : require
precision mediump float;
varying highp vec2 vTextureCoord;
uniform samplerExternalOES inputImageTexture;

uniform lowp float iGlobalTime ;
uniform lowp float constant ;

uniform lowp float frameWidth ;
uniform lowp float frameHeight ;

//const lowp vec2 iMouse = vec2(0.5,0.5);   // Need Control
//const lowp float speed = 1.0 ;   // Need Control


void main()
{
    lowp vec2 U = vTextureCoord;
    lowp vec2 iResolution = vec2(1.0,1.0);
    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;
    lowp vec4 fragColor;


    fragColor = texture2D(inputImageTexture, vTextureCoord);
    float color = (fragColor.r + fragColor.g + fragColor.b )/ 3.0;

   gl_FragColor =  vec4(color,color,color,fragColor.a);

}