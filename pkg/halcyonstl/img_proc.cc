#include "include/com_jackmeng_util_sys_sys_ImageProcessor.h"

JNIEXPORT jint JNICALL
Java_com_jackmeng_util_sys_sys_1ImageProcessor_img_1accent_1color(
    JNIEnv *env, jclass obj, jintArray pixels) {
  jsize len = env->GetArrayLength(pixels);
  jintArray freqs = env->NewIntArray(len);
  jint *px = env->GetIntArrayElements(pixels, NULL);
  jint accent = px[0];
  jint freq = 1;
  for (int i = 0; i < len; i++) {
    jint pix = px[i];
    if (px[i] == accent)
      freq++;
    else if (px[i] != accent && freq == 0) {
      accent = pix;
      freq = 1;
    } else
      freq = (freq ^ 1) & 1;
  }
  env->ReleaseIntArrayElements(pixels, px, 0);
  return accent;
}

JNIEXPORT jintArray JNICALL
Java_com_jackmeng_util_sys_sys_1ImageProcessor_img_1accent_1color_1palette(
    JNIEnv *env, jclass obj, jintArray pixels, jint depth) {
  jsize len = env->GetArrayLength(pixels);
  jint *pixelsElements = env->GetIntArrayElements(pixels, NULL);
  jintArray palette = env->NewIntArray(depth);
  jint *paletteElements = env->GetIntArrayElements(palette, NULL);
  for (int i = 0; i < depth; i++) paletteElements[i] = -1;
  for (int i = 0; i < len; i++) {
    jint pixel = pixelsElements[i];
    if (paletteElements[depth - 1] == -1 || paletteElements[depth - 1] == pixel)
      paletteElements[depth - 1] = pixel;
    else {
      int idx = -1;
      for (int j = 0; j < depth; j++) {
        if (paletteElements[j] == pixel) {
          idx = j;
          break;
        }
      }
      if (idx == -1) {
        for (int j = depth - 1; j > 0; j--)
          paletteElements[j] = paletteElements[j - 1];
        paletteElements[0] = pixel;
      }
    }
  }
  env->ReleaseIntArrayElements(pixels, pixelsElements, 0);
  env->ReleaseIntArrayElements(palette, paletteElements, 0);
  return palette;
}