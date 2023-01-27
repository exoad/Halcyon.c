#include <algorithm>
#include <cmath>

#include "cstring"
#include "include/com_jackmeng_platform_sys_ImageProcessor.h"

JNIEXPORT jint JNICALL
Java_com_jackmeng_platform_sys_sys_1ImageProcessor_img_1accent_1color(
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
Java_com_jackmeng_platform_sys_sys_1ImageProcessor_img_1accent_1color_1palette(
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

void muc_fill(int *px, int tolerance, int desired, int target, int width,
              int height) {
  for (int i = 0; i < (width * height); i++) {
    int r = (px[i] & 0xff0000) >> 16;
    int g = (px[i] & 0x00ff00) >> 8;
    int b = px[i] & 0x0000ff;
    if (abs(r - target) <= tolerance && abs(g - target) <= tolerance &&
        abs(b - target) <= tolerance) {
      px[i] = desired;
    }
  }
}

void sharpen_tone(int *px, int strength, int width, int height) {
  int *cpy = new int[width * height];
  std::memcpy(cpy, px, width * height * sizeof(int));
  for (int i = 0; i < width * height; i++) {
    int x = i % width;
    int y = i / width;
    int s = 0;
    s += -1 * cpy[(x - 1) + (y - 1) * width];
    s += -1 * cpy[(x - 1) + (y + 0) * width];
    s += -1 * cpy[(x - 1) + (y + 1) * width];
    s += -1 * cpy[(x + 0) + (y - 1) * width];
    s += 9 * cpy[(x + 0) + (y + 0) * width];
    s += -1 * cpy[(x + 0) + (y + 1) * width];
    s += -1 * cpy[(x + 1) + (y - 1) * width];
    s += -1 * cpy[(x + 1) + (y + 0) * width];
    s += -1 * cpy[(x + 1) + (y + 1) * width];
    px[i] = std::max(0, std::min(s * strength, 255));
  }
  delete[] cpy;
}

void tint(int *px, int tint, int strength, int width, int height) {
  for (int i = 0; i < width * height; i++) {
    int r = (px[i] & 0xff0000) >> 16;
    int g = (px[i] & 0x00ff00) >> 8;
    int b = px[i] & 0x0000ff;
    int tr = (tint & 0xff0000) >> 16;
    int tg = (tint & 0x00ff00) >> 8;
    int tb = tint & 0x0000ff;
    r = (r * (255 - strength) + tr * strength) / 255;
    g = (g * (255 - strength) + tg * strength) / 255;
    long a(0);
    b = (b * (255 - strength) + tb * strength) / 255;
    px[i] = (r << 16) | (g << 8) | b;
  }
}

void gaussian_blur_1(int *pixels, int strength, int width, int height) {}
