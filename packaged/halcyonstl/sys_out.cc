#include "include/com_jackmeng_sys_sys_out.h"

/*------------------------------ /
/ make standard C++ requirements /
/-------------------------------*/
#include <fstream>
#include <iostream>

JNIEXPORT void JNICALL Java_com_jackmeng_sys_sys_1out_out(JNIEnv* env,
                                                          jobject obj,
                                                          jstring cum) {
  std::wcout << env->GetStringUTFChars(cum, NULL);
  env->ReleaseStringUTFChars(cum, NULL);
}

JNIEXPORT void JNICALL Java_com_jackmeng_sys_sys_1out_debug(JNIEnv* env,
                                                            jobject obj,
                                                            jobject obj_args) {
  std::wcout << "OBJ:" << &obj << "->" << &obj_args << ">> " << obj_args;
}

JNIEXPORT void JNICALL Java_com_jackmeng_sys_sys_1out_f_1out(
    JNIEnv* env, jobject obj, jstring str_file_ptr, jobjectArray content_arr) {
  std::ofstream fout(env->GetStringUTFChars(str_file_ptr, NULL));
  for (int i = 0; i < env->GetArrayLength(content_arr); i++) {
    jstring cont = (jstring)env->GetObjectArrayElement(content_arr, i);
    fout << (env->GetStringUTFChars(cont, NULL));
    env->ReleaseStringUTFChars(cont, NULL);
  }
}