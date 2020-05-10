package com.jpventura.data.ktx

import com.google.gson.Gson
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.URI

@Throws(IllegalArgumentException::class)
fun <T> Gson.fromJson(file: File, classOfT: Class<T>): T =
    fromJson(file.bufferedReader(), classOfT)

@Throws(IllegalArgumentException::class)
fun <T> Gson.fromJson(file: File, type: Type): T =
    fromJson(file.bufferedReader(), type)

@Throws(IllegalArgumentException::class)
fun <T> Gson.fromJson(stream: InputStream, classOfT: Class<T>): T =
    this.fromJson(InputStreamReader(stream, Charsets.UTF_8), classOfT)

@Throws(IllegalArgumentException::class)
fun <T> Gson.fromJson(stream: InputStream, type: Type): T =
    this.fromJson(InputStreamReader(stream, Charsets.UTF_8), type)

@Throws(IllegalArgumentException::class)
fun <T> Gson.foo(uri: URI, classOfT: Class<T>): T =
    fromJson(File(uri), classOfT)

@Throws(IllegalArgumentException::class)
fun <T> Gson.foo(uri: URI, type: Type): T =
    this.fromJson(File(uri), type)
