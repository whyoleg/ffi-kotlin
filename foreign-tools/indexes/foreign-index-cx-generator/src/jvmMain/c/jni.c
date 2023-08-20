#include <jni.h>
#include <libCxIndexGenerator_api.h>

JNIEXPORT void JNICALL Java_dev_whyoleg_foreign_index_cx_generator_CxIndexGenerator_generate (JNIEnv* env, jclass jclss,
  jstring argumentsString,
  jstring resultPathString
) {
    const char* argumentsChars = (*env)->GetStringUTFChars(env, argumentsString, NULL);
    const char* resultPathChars = (*env)->GetStringUTFChars(env, resultPathString, NULL);
    generateCxIndex(argumentsChars, resultPathChars);
    (*env)->ReleaseStringUTFChars(env, argumentsString, argumentsChars);
    (*env)->ReleaseStringUTFChars(env, resultPathString, resultPathChars);
}
