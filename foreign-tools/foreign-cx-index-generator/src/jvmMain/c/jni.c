#include <jni.h>
#include <stdlib.h>
#include <libCxIndexGenerator_api.h>

JNIEXPORT jbyteArray JNICALL Java_dev_whyoleg_foreign_cx_index_generator_CxIndexGenerator_generate (JNIEnv* env, jclass jclss,
  jbyteArray argumentsBytes
) {
    // get ByteArray raw data
    jsize argumentsBytesSize = (*env)->GetArrayLength(env, argumentsBytes);
    jbyte* argumentsBytesElements = (*env)->GetByteArrayElements(env, argumentsBytes, NULL);

    // call K/N exported function
    jsize resultBytesSize;
    jbyte* resultBytesElements = generateCxIndex(argumentsBytesElements, argumentsBytesSize, &resultBytesSize);

    // release arguments
    (*env)->ReleaseByteArrayElements(env, argumentsBytes, argumentsBytesElements, 0);

    if (resultBytesElements == NULL) return NULL;

    // create ByteArray from raw data
    jbyteArray resultBytes = (*env)->NewByteArray(env, resultBytesSize);
    (*env)->SetByteArrayRegion(env, resultBytes, 0, resultBytesSize, resultBytesElements);

    // release result bytes allocated via malloc
    free(resultBytesElements);

    return resultBytes;
}
