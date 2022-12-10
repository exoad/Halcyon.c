#include <cmath>

#include "include/com_jackmeng_tailwind_sys_TailwindMath.h"

JNIEXPORT void JNICALL
Java_com_jackmeng_tailwind_sys_1TailwindMath_normalize_1mantissa_1iee(
    JNIEnv *env, jclass obj, jdoubleArray arr) {
  jsize len = env->GetArrayLength(arr);
  jdouble *ele = env->GetDoubleArrayElements(arr, NULL);
  for (int i = 0; i < len; i++) {
    if (ele[i] != 0.0) {
      double div = ele[i];
      for (int j = i; j < len; j++) ele[j] /= div;
      break;
    }
  }
  env->ReleaseDoubleArrayElements(arr, ele, 0);
}

JNIEXPORT jfloatArray JNICALL
Java_com_jackmeng_tailwind_sys_1TailwindMath_window_1func_11(
    JNIEnv *env, jclass obj, jfloatArray samples, jint type) {
  jsize len = env->GetArrayLength(samples);
  jfloatArray windowed = env->NewFloatArray(len);
  jfloat *e = env->GetFloatArrayElements(samples, NULL);
  jfloat *window_elements = env->GetFloatArrayElements(windowed, NULL);
  for (int i = 0; i < len; i++) {
    float curr = type == 0   ? 1.0F
                 : type == 1 ? 0.5F * (1.0F - cos(2.0 * M_PI * i / (len - 1)))
                 : type == 2 ? 0.54F - 0.46F * cos(2.0 * M_PI * i / (len - 1))
                             : 0.0F;
    window_elements[i] = e[i] * curr;
  }
  env->ReleaseFloatArrayElements(samples, e, 0);
  env->ReleaseFloatArrayElements(windowed, window_elements, 0);
  return windowed;
}