package dev.ultreon.mcgdx.impl;

import org.lwjgl.opengl.*;

class RenderState {

    private boolean blend;
    private boolean depthTest;
    private boolean cullFace;
    private boolean stencilTest;
    private boolean scissorTest;

    private int blendSrcRGB;
    private int blendDstRGB;
    private int blendSrcAlpha;
    private int blendDstAlpha;
    private int blendEquationRGB;
    private int blendEquationAlpha;
    private int depthFunc;
    private int stencilFunc;
    private int stencilRef;
    private int cullFaceMode;
    
    private float lineWidth;
    
    private int polygonMode;
    private int[] scissorBox = new int[4];

    public void restore() {
        if (this.blend) {
            GL20.glEnable(GL20.GL_BLEND);
            GL20.glBlendFuncSeparate(this.blendSrcRGB, this.blendDstRGB, this.blendSrcAlpha, this.blendDstAlpha);
            GL20.glBlendEquationSeparate(this.blendEquationRGB, this.blendEquationAlpha);
        } else {
            GL20.glDisable(GL20.GL_BLEND);
        }
        if (this.depthTest) {
            GL20.glEnable(GL20.GL_DEPTH_TEST);
            GL20.glDepthFunc(this.depthFunc);
        } else {
            GL20.glDisable(GL20.GL_DEPTH_TEST);
        }
        if (this.cullFace) {
            GL20.glEnable(GL20.GL_CULL_FACE);
            GL20.glCullFace(this.cullFaceMode);
        } else {
            GL20.glDisable(GL20.GL_CULL_FACE);
        }
        if (this.stencilTest) {
            GL20.glEnable(GL20.GL_STENCIL_TEST);
            GL20.glStencilFunc(this.stencilFunc, this.stencilRef, -1);
            GL20.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);
        } else {
            GL20.glDisable(GL20.GL_STENCIL_TEST);
        }
        if (this.scissorTest) {
            GL20.glEnable(GL20.GL_SCISSOR_TEST);
            GL20.glScissor(this.scissorBox[0], this.scissorBox[1], this.scissorBox[2], this.scissorBox[3]);
        } else {
            GL20.glDisable(GL20.GL_SCISSOR_TEST);
        }

        GL20.glLineWidth(this.lineWidth);
        GL20.glPolygonMode(GL20.GL_FRONT_AND_BACK, this.polygonMode);

        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL20.glBindTexture(GL20.GL_TEXTURE_2D, 0);
        GL20.glBindTexture(GL20.GL_TEXTURE_3D, 0);
        GL20.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, 0);
    }
    
    public void backup() {
        this.blend = GL20.glIsEnabled(GL20.GL_BLEND);
        this.depthTest = GL20.glIsEnabled(GL20.GL_DEPTH_TEST);
        this.cullFace = GL20.glIsEnabled(GL20.GL_CULL_FACE);
        this.stencilTest = GL20.glIsEnabled(GL20.GL_STENCIL_TEST);
        this.scissorTest = GL20.glIsEnabled(GL20.GL_SCISSOR_TEST);

        this.blendSrcRGB = GL20.glGetInteger(GL20.GL_BLEND_SRC_RGB);
        this.blendDstRGB = GL20.glGetInteger(GL20.GL_BLEND_DST_RGB);
        this.blendSrcAlpha = GL20.glGetInteger(GL20.GL_BLEND_SRC_ALPHA);
        this.blendDstAlpha = GL20.glGetInteger(GL20.GL_BLEND_DST_ALPHA);
        this.blendEquationRGB = GL20.glGetInteger(GL20.GL_BLEND_EQUATION_RGB);
        this.blendEquationAlpha = GL20.glGetInteger(GL20.GL_BLEND_EQUATION_ALPHA);
        this.depthFunc = GL20.glGetInteger(GL20.GL_DEPTH_FUNC);
        this.stencilFunc = GL20.glGetInteger(GL20.GL_STENCIL_FUNC);
        this.stencilRef = GL20.glGetInteger(GL20.GL_STENCIL_REF);
        
        this.polygonMode = GL20.glGetInteger(GL32.GL_POLYGON_MODE);

        this.lineWidth = GL20.glGetFloat(GL20.GL_LINE_WIDTH);
        
        GL20.glGetIntegerv(GL20.GL_SCISSOR_BOX, this.scissorBox);

        this.cullFaceMode = GL20.glGetInteger(GL20.GL_CULL_FACE_MODE);
    }
}
