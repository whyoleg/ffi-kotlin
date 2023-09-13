#include <jni.h>
#include <stdlib.h>
#include <libCxCompiler_api.h>

JNIEXPORT jbyteArray JNICALL Java_dev_whyoleg_foreign_tooling_cx_compiler_CxCompilerJni_call (JNIEnv* env, jclass jclss,
  jbyteArray requestBytes
) {
    // get ByteArray raw data
    jsize requestBytesSize = (*env)->GetArrayLength(env, requestBytes);
    jbyte* requestBytesElements = (*env)->GetByteArrayElements(env, requestBytes, NULL);

    // call K/N exported function
    jbyte* responseBytesElements;
    jsize responseBytesSize = callCxCompiler(requestBytesElements, requestBytesSize, &responseBytesElements);

    // release request
    (*env)->ReleaseByteArrayElements(env, requestBytes, requestBytesElements, 0);

    if (responseBytesElements == NULL) return NULL;
    if (responseBytesSize <= 0) return NULL;

    // create ByteArray from raw data
    jbyteArray responseBytes = (*env)->NewByteArray(env, responseBytesSize);
    (*env)->SetByteArrayRegion(env, responseBytes, 0, responseBytesSize, responseBytesElements);

    // release response bytes allocated via malloc
    free(responseBytesElements);

    return responseBytes;
}
