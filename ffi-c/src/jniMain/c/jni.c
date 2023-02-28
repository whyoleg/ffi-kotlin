#include <jni.h>

JNIEXPORT jobject JNICALL Java_dev_whyoleg_ffi_JNI_getByteBufferFromPointer (JNIEnv* env, jclass jclss,
  jlong p_pointer,
  jint p_size
) {
    return (*env)->NewDirectByteBuffer(env, (void*)p_pointer, p_size);
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_JNI_getPointerFromByteBuffer (JNIEnv* env, jclass jclss,
  jobject buffer
) {
    return (jlong)(*env)->GetDirectBufferAddress(env, buffer);
}

JNIEXPORT jstring JNICALL Java_dev_whyoleg_ffi_JNI_getStringFromPointer (JNIEnv* env, jclass jclss,
  jlong p_pointer
) {
    return (*env)->NewStringUTF(env, (char*)p_pointer);
}
