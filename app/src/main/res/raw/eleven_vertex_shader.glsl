attribute vec4 a_Position;		// Per-vertex position information we will pass in.
//attribute vec4 a_Color;			// Per-vertex color information we will pass in.
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in.
attribute vec2 a_TexCoordinate2; // Per-vertex texture coordinate information we will pass in.

//varying vec3 v_Position;		// This will be passed into the fragment shader.
//varying vec4 v_Color;			// This will be passed into the fragment shader.
varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.
varying vec2 v_TexCoordinate2;   // This will be passed into the fragment shader.

// The entry point for our vertex shader.
void main()
{
//	    v_Color = a_Color;
		vec4 tmp_Position = a_Position;// * vec4(0.75,0.7,0.6,1.0);
		gl_Position =  tmp_Position;
		v_TexCoordinate = a_TexCoordinate;
		v_TexCoordinate2 = a_TexCoordinate2;

}