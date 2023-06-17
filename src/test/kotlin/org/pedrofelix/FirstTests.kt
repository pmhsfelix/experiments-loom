package org.pedrofelix

import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.StructuredTaskScope
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirstTests {

    @Test
    fun `can use virtual threads`() {
        Executors.newVirtualThreadPerTaskExecutor().use { executor ->
            val res: Future<Boolean> = executor.submit<Boolean> {
                Thread.currentThread().isVirtual
            }
            assertTrue(res.get())
        }
    }

    @Test
    fun `can use structured concurrency`() {
        StructuredTaskScope.ShutdownOnFailure().use { scope ->

            val task0 = scope.fork {
                Thread.sleep(Duration.ofSeconds(1))
                "hello"
            }
            val task1 = scope.fork {
                Thread.sleep(Duration.ofSeconds(1))
                " world"
            }
            scope.join()
            scope.throwIfFailed()

            assertEquals("hello world", task0.get() + task1.get())
        }
    }

    @Test
    fun `can use scoped values`() {

        ScopedValue.runWhere(V, "hello") {
            StructuredTaskScope.ShutdownOnFailure().use { scope ->

                scope.fork {
                    assertEquals("hello", V.get())

                }
                scope.fork {
                    assertEquals("hello", V.get())

                    ScopedValue.runWhere(V, "world") {
                        assertEquals("world", V.get())
                    }
                }
                scope.join()
                scope.throwIfFailed()
            }
        }
    }

    companion object {
        private val V = ScopedValue.newInstance<String>()
    }
}