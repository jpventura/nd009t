package com.jpventura.data.ktx

import org.kodein.di.KodeinAware
import java.io.InputStream

@Throws(IllegalArgumentException::class)
fun KodeinAware.getResourceAsStream(pathname: String): InputStream {
    return kotlin.requireNotNull(this.javaClass.classLoader?.getResourceAsStream(pathname)) {
        "File not found: $pathname"
    }
}
