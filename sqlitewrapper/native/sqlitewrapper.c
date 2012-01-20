
#include <jni.h>
#include <stdio.h>

#include "sqlite3.h"

#include "sqlitewrapper_Database.h"
#include "sqlitewrapper_Statement.h"


JNIEXPORT void JNICALL Java_sqlitewrapper_Database_openInternal
  (JNIEnv * env, jclass cls, jlongArray jresult, jstring jpath, jint jmode) {

	sqlite3 * db;
	const char * path = (*env)->GetStringUTFChars(env, jpath, 0);

	if (path == NULL) {
		  return; /* OutOfMemoryError already thrown */
	}

	jlong* result = (*env)->GetPrimitiveArrayCritical(env, jresult, NULL);
	result[0] = (jlong) sqlite3_open(path, &db);
	result[1] = (jlong) ((int) db);

	(*env)->ReleasePrimitiveArrayCritical(env, jresult, result, 0);
	(*env)->ReleaseStringUTFChars(env, jpath, path);
}

JNIEXPORT void JNICALL Java_sqlitewrapper_Statement_prepareInternal
  (JNIEnv * env, jclass cls, jlongArray jresult, jlong db_ptr, jstring jsql) {

  sqlite3 * db = (sqlite3 *) ((long) db_ptr);
  sqlite3_stmt * stmt;

	const char * sql = (*env)->GetStringUTFChars(env, jsql, 0);

	if (sql == NULL) {
		  return; /* OutOfMemoryError already thrown */
	}

	jlong* result = (*env)->GetPrimitiveArrayCritical(env, jresult, NULL);
	result[0] = (jlong) sqlite3_prepare_v2(db, sql, -1, &stmt, NULL);
	result[1] = (jlong) ((int) stmt);

	(*env)->ReleasePrimitiveArrayCritical(env, jresult, result, 0);
	(*env)->ReleaseStringUTFChars(env, jsql, sql);
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Database_execInternal
  (JNIEnv * env, jobject obj, jlong ptr, jstring jsql) {

	const char * sql = (*env)->GetStringUTFChars(env, jsql, 0);
	if (sql == NULL) {
		  return 0; /* OutOfMemoryError already thrown */
	}
	sqlite3 * db = (sqlite3 *) ((long) ptr);
  sqlite3_stmt * stmt;
	int rc = sqlite3_prepare_v2(db, sql, -1, &stmt, NULL);
	if (SQLITE_OK == rc) {
		rc = sqlite3_step(stmt);
		sqlite3_finalize(stmt);
	}	
	(*env)->ReleaseStringUTFChars(env, jsql, sql);
	return rc;
}


JNIEXPORT jint JNICALL Java_sqlitewrapper_Database_closeInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_close((sqlite3 *) ((long) ptr));
}


JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_stepInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_step((sqlite3_stmt *) ((long) ptr));
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_resetInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_reset((sqlite3_stmt *) ((long) ptr));
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_clearBindingsInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_clear_bindings((sqlite3_stmt *) ((long) ptr));
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_resetAndClearBindingsInternal
  (JNIEnv * env, jobject obj, jlong ptr) {
	
	sqlite3_stmt * stmt = (sqlite3_stmt *) ((long) ptr);
	int rc = sqlite3_reset(stmt);
	if (SQLITE_OK != rc) {
		return rc;
	}
	return sqlite3_clear_bindings(stmt);
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_bindIntInternal
  (JNIEnv * env, jobject obj, jlong ptr, jint col, jint value) {

	return sqlite3_bind_int((sqlite3_stmt *) ((long) ptr), col, value);
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_bindDoubleInternal
  (JNIEnv * env, jobject obj, jlong ptr, jint col, jdouble value) {

	return sqlite3_bind_double((sqlite3_stmt *) ((long) ptr), col, value);
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_bindBlobInternal
  (JNIEnv * env, jobject obj, jlong ptr, jint col, jbyteArray value, jint size) {

  jbyte* buffer = (*env)->GetPrimitiveArrayCritical(env, value, NULL);
	if (buffer == NULL) {
		return 0;
	}
	sqlite3_stmt * stmt = (sqlite3_stmt *) ((long) ptr);
	int rc = sqlite3_bind_blob(stmt, col, buffer, size, SQLITE_TRANSIENT);
  (*env)->ReleasePrimitiveArrayCritical(env, value, buffer, JNI_ABORT);
	return rc;
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_dataCountInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_data_count((sqlite3_stmt *) ((long) ptr));
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_columnCountInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_column_count((sqlite3_stmt *) ((long) ptr));
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_columnIntInternal
  (JNIEnv * env, jobject obj, jlong ptr, jint col) {

	return sqlite3_column_int((sqlite3_stmt *) ((long) ptr), col);
}

JNIEXPORT jdouble JNICALL Java_sqlitewrapper_Statement_columnDoubleInternal
  (JNIEnv * env, jobject obj, jlong ptr, jint col) {

	return sqlite3_column_double((sqlite3_stmt *) ((long) ptr), col);
}

JNIEXPORT jbyteArray JNICALL Java_sqlitewrapper_Statement_columnBlobInternal
  (JNIEnv * env, jobject obj, jlong ptr, jint col) {

	sqlite3_stmt * stmt =	(sqlite3_stmt *) ((long) ptr);

	const void * data = sqlite3_column_blob(stmt, col);
	if (data == NULL) {
		return NULL;
	}

	int size = sqlite3_column_bytes(stmt, col);

	jbyteArray result = (*env)->NewByteArray(env, size);
	if (result == NULL) {
		  return NULL; /* out of memory error thrown */
	}
	(*env)->SetByteArrayRegion(env, result, 0, size, data);
	return result;
}

JNIEXPORT jint JNICALL Java_sqlitewrapper_Statement_closeInternal
  (JNIEnv * env, jobject obj, jlong ptr) {

	return sqlite3_finalize((sqlite3_stmt *) ((long) ptr));
}


