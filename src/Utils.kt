import java.lang.AssertionError
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.exp

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun <T>assertEqual(actual: T, expected: T) {
    if (actual != expected) {
        throw AssertionError("Expected $expected, got $actual")
    }
}

fun <T>  List<T>.windowWhen(pred: (T) -> Boolean): List<List<T>> {
    val chunks: MutableList<MutableList<T>> = mutableListOf(mutableListOf())
    forEach {
        if (pred(it)) {
            chunks.add(mutableListOf())
        } else {
            chunks.last().add(it)
        }
    }
    return chunks
}
