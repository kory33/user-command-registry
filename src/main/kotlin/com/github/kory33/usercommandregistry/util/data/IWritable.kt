package com.github.kory33.usercommandregistry.util.data

/**
 * An interface for a writable object.
 *
 * An implementation of this interface is expected to hold
 * its own data to write and the target to which it writes data.
 */
interface IWritable {
    /**
     * Write data to the desired target of the implementation.
     *
     * This function may or may not be implemented asynchronously.
     */
    fun writeData()
}