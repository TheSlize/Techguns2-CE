package techguns.client.models.guns;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import techguns.client.models.ModelMultipart;
import techguns.client.render.TGRenderHelper;
import techguns.client.render.TGRenderHelper.RenderType;
import techguns.util.MathUtil;

public class ModelNDR extends ModelMultipart {
    public ModelRenderer shape14;
    public ModelRenderer shape15;
    public ModelRenderer cube_r1;
    public ModelRenderer cube_r2;
    public ModelRenderer shape14_1;
    public ModelRenderer shape14_2;
    public ModelRenderer GLOW;
    public ModelRenderer shape14_3;
    public ModelRenderer shape1;
    public ModelRenderer shape1_1;
    public ModelRenderer sixside;
    public ModelRenderer shape1__4;
    public ModelRenderer shape1__3;
    public ModelRenderer shape1__1;
    public ModelRenderer shape1_;
    public ModelRenderer shape1__2;
    public ModelRenderer shape14_4;
    public ModelRenderer shape14_5;
    public ModelRenderer shape14_6;
    public ModelRenderer shape14_7;
    public ModelRenderer shape14_8;
    public ModelRenderer shape2;
    public ModelRenderer glowA1; // Переименовано из glow_A1
    public ModelRenderer shape14_10;
    public ModelRenderer shape84;
    public ModelRenderer shape84_1;
    public ModelRenderer shape84_2;
    public ModelRenderer shape84_3;
    public ModelRenderer shape84_4;
    public ModelRenderer shape84_5;
    public ModelRenderer shape84_6;
    public ModelRenderer shape84_7;
    public ModelRenderer shape99;
    public ModelRenderer shape99_1;
    public ModelRenderer shape99_2;
    public ModelRenderer shape1_2;
    public ModelRenderer shape1_3;
    public ModelRenderer shape1_4;
    public ModelRenderer shape1_5;
    public ModelRenderer shape1_6;
    public ModelRenderer shape1_7;
    public ModelRenderer shape1_8;
    public ModelRenderer shape1_9;
    public ModelRenderer shape1_10;
    public ModelRenderer shape1_11;
    public ModelRenderer sixside_1;
    public ModelRenderer shape1__9;
    public ModelRenderer shape1__5;
    public ModelRenderer shape1__6;
    public ModelRenderer shape1__7;
    public ModelRenderer shape1__8;
    public ModelRenderer shape14_11;
    public ModelRenderer shape14_12;
    public ModelRenderer shape45_;
    public ModelRenderer shape45_1;
    public ModelRenderer shape45;
    public ModelRenderer shape48_1;
    public ModelRenderer shape48;
    public ModelRenderer shape48_2;
    public ModelRenderer shape58;
    public ModelRenderer shape59;
    public ModelRenderer shape60;
    public ModelRenderer shape58_1;
    public ModelRenderer shape59_1;
    public ModelRenderer shape60_1;
    public ModelRenderer shape58_2;
    public ModelRenderer shape59_2;
    public ModelRenderer shape60_2;
    public ModelRenderer shape83;
    public ModelRenderer shape82; // Бывший shape3

    public ModelNDR() {
        this.textureWidth = 128;
        this.textureHeight = 64;

        shape14 = new ModelRenderer(this, 76, 39);
        shape14.setRotationPoint(-5.0F, 2.0F, -2.0F);
        shape14.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4, 0.0F);

        shape15 = new ModelRenderer(this, 38, 23);
        shape15.setRotationPoint(-4.0F, -2.0F, -3.0F);
        shape15.addBox(0.0F, 0.0F, -1.0F, 17, 4, 8, 0.0F);

        cube_r1 = new ModelRenderer(this, 44, 39);
        cube_r1.setRotationPoint(16.0F, 0.5F, 6.0F);
        shape15.addChild(cube_r1);
        setRotation(cube_r1, -0.6545F, 0.0F, 0.0F);
        cube_r1.addBox(-16.0F, -1.0F, -1.0F, 17, 2, 1, 0.0F);

        cube_r2 = new ModelRenderer(this, 44, 39);
        cube_r2.setRotationPoint(16.0F, 0.0F, 1.0F);
        shape15.addChild(cube_r2);
        setRotation(cube_r2, 0.6545F, 0.0F, 0.0F);
        cube_r2.addBox(-16.0F, -1.0F, -1.0F, 17, 2, 1, 0.0F);

        shape14_1 = new ModelRenderer(this, 76, 39);
        shape14_1.setRotationPoint(3.0F, -3.0F, -2.0F);
        shape14_1.addBox(10.0F, 0.0F, 0.0F, 1, 1, 4, 0.0F);

        shape14_2 = new ModelRenderer(this, 76, 39);
        shape14_2.setRotationPoint(3.0F, 2.0F, -2.0F);
        shape14_2.addBox(10.0F, 0.0F, 0.0F, 1, 1, 4, 0.0F);

        GLOW = new ModelRenderer(this, 65, 12);
        GLOW.setRotationPoint(-4.0F, -2.0F, -2.0F);
        GLOW.addBox(0.0F, 0.0F, -1.0F, 17, 4, 6, 0.0F);

        shape14_3 = new ModelRenderer(this, 74, 37);
        shape14_3.setRotationPoint(3.0F, -2.0F, -3.0F);
        shape14_3.addBox(10.0F, 0.0F, 0.0F, 1, 4, 6, 0.0F);

        shape1 = new ModelRenderer(this, 48, 5);
        shape1.setRotationPoint(17.0F, -3.46F, 2.0F);
        setRotation(shape1, -1.0472F, 0.0F, 0.0F);
        shape1.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);

        shape1_1 = new ModelRenderer(this, 50, 0);
        shape1_1.setRotationPoint(17.0F, -3.46F, -2.0F);
        shape1_1.addBox(0.0F, 0.0F, 0.0F, 3, 1, 4, 0.0F);

        sixside = new ModelRenderer(this, 0, 0);
        sixside.setRotationPoint(-8.0F, -4.33F, -2.5F);
        sixside.addBox(0.0F, 0.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1__4 = new ModelRenderer(this, 0, 0);
        shape1__4.setRotationPoint(0.0F, 8.66F, 0.0F);
        sixside.addChild(shape1__4);
        shape1__4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1__3 = new ModelRenderer(this, 0, 0);
        shape1__3.setRotationPoint(0.0F, 8.66F, 5.0F);
        sixside.addChild(shape1__3);
        setRotation(shape1__3, 1.0472F, 0.0F, 0.0F);
        shape1__3.addBox(0.0F, -3.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1__1 = new ModelRenderer(this, 0, 0);
        shape1__1.setRotationPoint(0.0F, 0.0F, 5.0F);
        sixside.addChild(shape1__1);
        setRotation(shape1__1, -1.0472F, 0.0F, 0.0F);
        shape1__1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1_ = new ModelRenderer(this, 0, 0);
        shape1_.setRotationPoint(0.0F, 0.0F, 0.0F);
        sixside.addChild(shape1_);
        setRotation(shape1_, 1.0472F, 0.0F, 0.0F);
        shape1_.addBox(0.0F, 0.0F, -5.0F, 3, 3, 5, 0.0F);

        shape1__2 = new ModelRenderer(this, 0, 0);
        shape1__2.setRotationPoint(0.0F, 8.66F, 0.0F);
        sixside.addChild(shape1__2);
        setRotation(shape1__2, -1.0472F, 0.0F, 0.0F);
        shape1__2.addBox(0.0F, -3.0F, -5.0F, 3, 3, 5, 0.0F);

        shape14_4 = new ModelRenderer(this, 16, 16);
        shape14_4.setRotationPoint(-20.0F, -5.0F, -2.0F);
        shape14_4.addBox(0.0F, 0.0F, 0.0F, 14, 1, 4, 0.0F);

        shape14_5 = new ModelRenderer(this, 28, 4);
        shape14_5.setRotationPoint(-13.8F, -8.6F, -2.0F);
        setRotation(shape14_5, 0.0F, 0.0F, 0.7854F);
        shape14_5.addBox(0.0F, 0.0F, 0.0F, 1, 4, 4, 0.0F);

        shape14_6 = new ModelRenderer(this, 20, 12);
        shape14_6.setRotationPoint(-15.0F, -7.0F, -1.5F);
        shape14_6.addBox(0.0F, 0.0F, 0.0F, 6, 1, 3, 0.0F);

        shape14_7 = new ModelRenderer(this, 20, 21);
        shape14_7.setRotationPoint(-16.0F, -6.0F, -1.5F);
        shape14_7.addBox(0.0F, 0.0F, 0.0F, 10, 1, 3, 0.0F);

        shape14_8 = new ModelRenderer(this, 76, 39);
        shape14_8.setRotationPoint(-5.0F, -3.0F, -2.0F);
        shape14_8.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4, 0.0F);

        shape2 = new ModelRenderer(this, 67, 31);
        shape2.setRotationPoint(0.0F, 24.0F, 0.0F);
        shape2.addBox(-4.0F, -27.0F, -2.0F, 17, 6, 4, 0.0F);

        glowA1 = new ModelRenderer(this, 71, 5);
        glowA1.setRotationPoint(0.0F, 0.0F, 0.2F);
        setRotation(glowA1, 0.4363F, 0.0F, 0.0F);
        glowA1.addBox(-2.0F, -6.0F, -0.5F, 14, 5, 1, 0.0F);

        shape14_10 = new ModelRenderer(this, 74, 42);
        shape14_10.setRotationPoint(-5.0F, -2.0F, -3.0F);
        shape14_10.addBox(0.0F, 0.0F, 0.0F, 1, 4, 6, 0.0F);

        shape84 = new ModelRenderer(this, 34, 25);
        shape84.setRotationPoint(-25.0F, -1.0F, -1.5F);
        shape84.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);

        shape84_1 = new ModelRenderer(this, 26, 25);
        shape84_1.setRotationPoint(-23.0F, -3.0F, -2.0F);
        shape84_1.addBox(0.0F, 0.0F, 0.0F, 2, 6, 4, 0.0F);

        shape84_2 = new ModelRenderer(this, 34, 25);
        shape84_2.setRotationPoint(-25.0F, -1.0F, 0.5F);
        shape84_2.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);

        shape84_3 = new ModelRenderer(this, 38, 28);
        shape84_3.setRotationPoint(-31.0F, 0.0F, -0.5F);
        shape84_3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);

        shape84_4 = new ModelRenderer(this, 34, 35);
        shape84_4.setRotationPoint(-26.0F, -2.0F, -0.5F);
        shape84_4.addBox(0.0F, 0.0F, 0.0F, 3, 4, 1, 0.0F);

        shape84_5 = new ModelRenderer(this, 40, 1);
        shape84_5.setRotationPoint(-31.0F, 0.0F, 0.0F);
        shape84_5.addBox(0.0F, 0.0F, 0.0F, 5, 4, 0, 0.0F);

        shape84_6 = new ModelRenderer(this, 12, 25);
        shape84_6.setRotationPoint(-32.0F, 0.0F, -0.5F);
        shape84_6.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);

        shape84_7 = new ModelRenderer(this, 16, 25);
        shape84_7.setRotationPoint(-32.0F, -2.0F, -0.5F);
        shape84_7.addBox(0.0F, 0.0F, 0.0F, 6, 2, 1, 0.0F);

        shape99 = new ModelRenderer(this, 12, 20);
        shape99.setRotationPoint(-5.5F, 5.0F, -2.5F);
        setRotation(shape99, 0.0F, 0.0F, -0.4098F);
        shape99.addBox(-1.0F, -1.0F, 0.0F, 1, 3, 1, 0.0F);

        shape99_1 = new ModelRenderer(this, 24, 36);
        shape99_1.setRotationPoint(-8.5F, 3.0F, -3.0F);
        shape99_1.addBox(0.0F, 0.0F, 0.0F, 3, 2, 2, 0.0F);

        shape99_2 = new ModelRenderer(this, 9, 10);
        shape99_2.setRotationPoint(-6.0F, 5.0F, -3.0F);
        shape99_2.addBox(-2.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F);

        shape1_2 = new ModelRenderer(this, 50, 0);
        shape1_2.setRotationPoint(17.0F, 3.46F, -2.0F);
        setRotation(shape1_2, -1.0472F, 0.0F, 0.0F);
        shape1_2.addBox(0.0F, -1.0F, -4.0F, 3, 1, 4, 0.0F);

        shape1_3 = new ModelRenderer(this, 48, 5);
        shape1_3.setRotationPoint(7.0F, 3.46F, -2.0F);
        shape1_3.addBox(10.0F, -1.0F, 0.0F, 4, 1, 4, 0.0F);

        shape1_4 = new ModelRenderer(this, 48, 5);
        shape1_4.setRotationPoint(17.0F, -3.46F, -2.0F);
        setRotation(shape1_4, 1.0472F, 0.0F, 0.0F);
        shape1_4.addBox(0.0F, 0.0F, -4.0F, 4, 1, 4, 0.0F);

        shape1_5 = new ModelRenderer(this, 52, 10);
        shape1_5.setRotationPoint(14.0F, 2.6F, -1.5F);
        setRotation(shape1_5, -1.0472F, 0.0F, 0.0F);
        shape1_5.addBox(0.0F, -2.0F, -3.0F, 3, 2, 3, 0.0F);

        shape1_6 = new ModelRenderer(this, 50, 0);
        shape1_6.setRotationPoint(17.0F, 3.46F, 2.0F);
        setRotation(shape1_6, 1.0472F, 0.0F, 0.0F);
        shape1_6.addBox(0.0F, -1.0F, 0.0F, 3, 1, 4, 0.0F);

        shape1_7 = new ModelRenderer(this, 52, 10);
        shape1_7.setRotationPoint(14.0F, -2.6F, 1.5F);
        setRotation(shape1_7, -1.0472F, 0.0F, 0.0F);
        shape1_7.addBox(0.0F, 0.0F, 0.0F, 3, 2, 3, 0.0F);

        shape1_8 = new ModelRenderer(this, 52, 10);
        shape1_8.setRotationPoint(14.0F, -2.6F, -1.5F);
        setRotation(shape1_8, 1.0472F, 0.0F, 0.0F);
        shape1_8.addBox(0.0F, 0.0F, -3.0F, 3, 2, 3, 0.0F);

        shape1_9 = new ModelRenderer(this, 52, 10);
        shape1_9.setRotationPoint(14.0F, -2.6F, -1.5F);
        shape1_9.addBox(0.0F, 0.0F, 0.0F, 3, 2, 3, 0.0F);

        shape1_10 = new ModelRenderer(this, 52, 10);
        shape1_10.setRotationPoint(4.0F, 2.6F, 1.5F);
        setRotation(shape1_10, 1.0472F, 0.0F, 0.0F);
        shape1_10.addBox(10.0F, -2.0F, 0.0F, 3, 2, 3, 0.0F);

        shape1_11 = new ModelRenderer(this, 52, 10);
        shape1_11.setRotationPoint(14.0F, 2.6F, -1.5F);
        shape1_11.addBox(0.0F, -2.0F, 0.0F, 3, 2, 3, 0.0F);

        sixside_1 = new ModelRenderer(this, 0, 0);
        sixside_1.setRotationPoint(-21.0F, -4.33F, -2.5F);
        sixside_1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1__9 = new ModelRenderer(this, 0, 0);
        shape1__9.setRotationPoint(0.0F, 8.66F, 0.0F);
        sixside_1.addChild(shape1__9);
        shape1__9.addBox(0.0F, -3.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1__5 = new ModelRenderer(this, 0, 0);
        shape1__5.setRotationPoint(0.0F, 0.0F, 0.0F);
        sixside_1.addChild(shape1__5);
        setRotation(shape1__5, 1.0472F, 0.0F, 0.0F);
        shape1__5.addBox(0.0F, 0.0F, -5.0F, 3, 3, 5, 0.0F);

        shape1__6 = new ModelRenderer(this, 0, 0);
        shape1__6.setRotationPoint(0.0F, 0.0F, 5.0F);
        sixside_1.addChild(shape1__6);
        setRotation(shape1__6, -1.0472F, 0.0F, 0.0F);
        shape1__6.addBox(0.0F, 0.0F, 0.0F, 3, 3, 5, 0.0F);

        shape1__7 = new ModelRenderer(this, 0, 0);
        shape1__7.setRotationPoint(0.0F, 7.66F, 0.0F);
        sixside_1.addChild(shape1__7);
        setRotation(shape1__7, -1.0472F, 0.0F, 0.0F);
        shape1__7.addBox(0.0F, -1.0036F, -4.4581F, 3, 3, 5, 0.0F);

        shape1__8 = new ModelRenderer(this, 0, 0);
        shape1__8.setRotationPoint(0.0F, 7.66F, 6.0F);
        sixside_1.addChild(shape1__8);
        setRotation(shape1__8, 1.0472F, 0.0F, 0.0F);
        shape1__8.addBox(0.0F, -2.0F, -1.0F, 3, 3, 5, 0.0F);

        shape14_11 = new ModelRenderer(this, 38, 5);
        shape14_11.setRotationPoint(-9.0F, -4.0F, -2.0F);
        shape14_11.addBox(0.0F, 0.0F, 0.0F, 1, 7, 4, 0.0F);

        shape14_12 = new ModelRenderer(this, 38, 5);
        shape14_12.setRotationPoint(-18.0F, -4.0F, -2.0F);
        shape14_12.addBox(0.0F, 0.0F, 0.0F, 1, 7, 4, 0.0F);

        shape45_ = new ModelRenderer(this, 34, 53);
        shape45_.setRotationPoint(-17.0F, -3.5F, -1.5F);
        shape45_.addBox(0.0F, -2.0F, -1.0F, 8, 6, 5, 0.0F);

        shape45_1 = new ModelRenderer(this, 0, 50);
        shape45_1.setRotationPoint(0.0F, 0.0F, 3.0F);
        shape45_.addChild(shape45_1);
        setRotation(shape45_1, -1.0472F, 0.0F, 0.0F);
        shape45_1.addBox(0.0F, -2.0F, -1.0F, 8, 6, 8, 0.0F);

        shape45 = new ModelRenderer(this, 0, 50);
        shape45.setRotationPoint(0.0F, 0.0F, 0.0F);
        shape45_.addChild(shape45);
        setRotation(shape45, 1.0472F, 0.0F, 0.0F);
        shape45.addBox(0.0F, -2.0F, -7.0F, 8, 6, 8, 0.0F);

        shape48_1 = new ModelRenderer(this, 38, 54);
        shape48_1.setRotationPoint(0.0F, 7.79F, 4.5F);
        shape45_.addChild(shape48_1);
        setRotation(shape48_1, 1.0472F, 0.0F, 0.0F);
        shape48_1.addBox(0.0F, -4.0F, 0.0F, 8, 6, 4, 0.0F);

        shape48 = new ModelRenderer(this, 34, 54);
        shape48.setRotationPoint(0.0F, 7.79F, -1.5F);
        shape45_.addChild(shape48);
        setRotation(shape48, -1.0472F, 0.0F, 0.0F);
        shape48.addBox(0.0F, -4.0F, -4.0F, 8, 6, 4, 0.0F);

        shape48_2 = new ModelRenderer(this, 0, 50);
        shape48_2.setRotationPoint(0.0F, 7.79F, -1.5F);
        shape45_.addChild(shape48_2);
        shape48_2.addBox(0.0F, -5.0F, -1.0F, 8, 6, 8, 0.0F);

        shape58 = new ModelRenderer(this, 24, 4);
        shape58.setRotationPoint(-6.5F, 0.0F, 0.0F);
        shape58.addBox(0.0F, -5.33F, -1.0F, 2, 2, 2, 0.0F);

        shape59 = new ModelRenderer(this, 20, 0);
        shape59.setRotationPoint(1.5F, -4.83F, -0.5F);
        shape58.addChild(shape59);
        shape59.addBox(0.0F, 0.0F, 0.0F, 19, 1, 1, 0.0F);

        shape60 = new ModelRenderer(this, 30, 2);
        shape60.setRotationPoint(10.5F, -4.83F, -0.5F);
        shape58.addChild(shape60);
        setRotation(shape60, 0.0F, 0.0F, 0.7854F);
        shape60.addBox(7.0711F, -7.0711F, 0.0F, 4, 1, 1, 0.0F);

        shape58_1 = new ModelRenderer(this, 18, 8);
        shape58_1.setRotationPoint(-6.5F, 0.0F, 0.0F);
        setRotation(shape58_1, 2.0944F, 0.0F, 0.0F);
        shape58_1.addBox(-1.0F, -5.33F, -1.0F, 3, 2, 2, 0.0F);

        shape59_1 = new ModelRenderer(this, 20, 0);
        shape59_1.setRotationPoint(1.5F, -4.83F, -0.5F);
        shape58_1.addChild(shape59_1);
        shape59_1.addBox(0.0F, 0.0F, 0.0F, 19, 1, 1, 0.0F);

        shape60_1 = new ModelRenderer(this, 30, 2);
        shape60_1.setRotationPoint(10.5F, -4.83F, -0.5F);
        shape58_1.addChild(shape60_1);
        setRotation(shape60_1, 0.0F, 0.0F, 0.7854F);
        shape60_1.addBox(7.0711F, -7.0711F, 0.0F, 4, 1, 1, 0.0F);

        shape58_2 = new ModelRenderer(this, 18, 8);
        shape58_2.setRotationPoint(-6.5F, 0.0F, 0.0F);
        setRotation(shape58_2, -2.0944F, 0.0F, 0.0F);
        shape58_2.addBox(-1.0F, -5.33F, -1.0F, 3, 2, 2, 0.0F);

        shape59_2 = new ModelRenderer(this, 20, 0);
        shape59_2.setRotationPoint(1.5F, -4.83F, -0.5F);
        shape58_2.addChild(shape59_2);
        shape59_2.addBox(0.0F, 0.0F, 0.0F, 19, 1, 1, 0.0F);

        shape60_2 = new ModelRenderer(this, 30, 2);
        shape60_2.setRotationPoint(10.5F, -4.83F, -0.5F);
        shape58_2.addChild(shape60_2);
        setRotation(shape60_2, 0.0F, 0.0F, 0.7854F);
        shape60_2.addBox(7.0711F, -7.0711F, 0.0F, 4, 1, 1, 0.0F);

        shape83 = new ModelRenderer(this, 17, 29);
        shape83.setRotationPoint(0.0F, -12.0F, 0.0F);
        shape58_2.addChild(shape83);
        setRotation(shape83, 0.0F, 0.7854F, 0.0F);
        // Множественные боксы для shape83
        shape83.addBox(-0.5F, 6.0F, -0.5F, 1, 1, 1, 0.0F);
        shape83.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);

        shape82 = new ModelRenderer(this, 23, 14);
        shape82.setRotationPoint(10.5F, 9.4904F, 0.0981F);
        setRotation(shape82, 0.0F, 0.0F, -3.1416F);
        shape82.addBox(-0.5F, 5.5096F, -0.5F, 5, 1, 1, 0.0F);
        shape82.addBox(-1.0F, 3.5096F, -1.0F, 6, 2, 2, 0.0F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, int ammoLeft,
                       float reloadProgress, TransformType transformType, int part, float fireProgress, float chargeProgress) {

        shape14.render(scale);
        shape15.render(scale);
        shape14_1.render(scale);
        shape14_2.render(scale);
        shape14_3.render(scale);
        shape1.render(scale);
        shape1_1.render(scale);
        sixside.render(scale);
        shape14_4.render(scale);
        shape14_5.render(scale);
        shape14_6.render(scale);
        shape14_7.render(scale);
        shape14_8.render(scale);
        shape2.render(scale);
        shape14_10.render(scale);
        shape84.render(scale);
        shape84_1.render(scale);
        shape84_2.render(scale);
        shape84_3.render(scale);
        shape84_4.render(scale);
        shape84_5.render(scale);
        shape84_6.render(scale);
        shape84_7.render(scale);
        shape99.render(scale);
        shape99_1.render(scale);
        shape99_2.render(scale);
        shape1_2.render(scale);
        shape1_3.render(scale);
        shape1_4.render(scale);
        shape1_5.render(scale);
        shape1_6.render(scale);
        shape1_7.render(scale);
        shape1_8.render(scale);
        shape1_9.render(scale);
        shape1_10.render(scale);
        shape1_11.render(scale);
        sixside_1.render(scale);
        shape14_11.render(scale);
        shape14_12.render(scale);
        shape45_.render(scale);
        shape58.render(scale);
        shape58_1.render(scale);
        shape58_2.render(scale);
        shape82.render(scale);

        TGRenderHelper.enableBlendMode(RenderType.ALPHA);
        GLOW.render(scale);

        if (fireProgress > 0) {
            GlStateManager.disableCull();

            GL11.glPushMatrix();
            double s = 0.90 + Math.sin(fireProgress*2.0*Math.PI)*0.1;
            GL11.glScaled(s, s, s);

            glowA1.render(scale);
            glowA1.rotateAngleX = 63.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = 117.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = 155.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = -155.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = -117.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = -63.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = -25.0f*((float)MathUtil.D2R);
            glowA1.render(scale);
            glowA1.rotateAngleX = 25.0f*((float)MathUtil.D2R);

            GL11.glPopMatrix();
            GlStateManager.enableCull();
        }
        TGRenderHelper.disableBlendMode(RenderType.ALPHA);
    }
}