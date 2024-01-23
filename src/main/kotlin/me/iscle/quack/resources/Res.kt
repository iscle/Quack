package me.iscle.quack.resources

import me.iscle.quack.InputStreamByteBuffer

class Res {

    class Value(
        // Number of bytes in this structure.
        val size: UShort,

        // Always set to 0.
        val res0: UByte,

        // Type of the data value.
        val dataType: UByte,

        // The data for this item, as interpreted according to dataType.
        val data: UInt,
    ) {
        companion object {
            fun parse(buffer: InputStreamByteBuffer): Value {
                val size = buffer.getUShort()
                val res0 = buffer.getUByte()
                val dataType = buffer.getUByte()
                val data = buffer.getUInt()
                return Value(size, res0, dataType, data)
            }
        }
    }

    companion object {
        // The 'data' is either 0 or 1, specifying this resource is either
// undefined or empty, respectively.
        const val TYPE_NULL: UByte = 0x00u
        // The 'data' holds a ResTable_ref, a reference to another resource
// table entry.
        const val TYPE_REFERENCE: UByte = 0x01u
        // The 'data' holds an attribute resource identifier.
        const val TYPE_ATTRIBUTE: UByte = 0x02u
        // The 'data' holds an index into the containing resource table's
// global value string pool.
        const val TYPE_STRING: UByte = 0x03u
        // The 'data' holds a single-precision floating point number.
        const val TYPE_FLOAT: UByte = 0x04u
        // The 'data' holds a complex number encoding a dimension value,
// such as "100in".
        const val TYPE_DIMENSION: UByte = 0x05u
        // The 'data' holds a complex number encoding a fraction of a
// container.
        const val TYPE_FRACTION: UByte = 0x06u
        // The 'data' holds a dynamic ResTable_ref, which needs to be
// resolved before it can be used like a TYPE_REFERENCE.
        const val TYPE_DYNAMIC_REFERENCE: UByte = 0x07u
        // The 'data' holds an attribute resource identifier, which needs to be resolved
// before it can be used like a TYPE_ATTRIBUTE.
        const val TYPE_DYNAMIC_ATTRIBUTE: UByte = 0x08u

        // Beginning of integer flavors...
        const val TYPE_FIRST_INT: UByte = 0x10u

        // The 'data' is a raw integer value of the form n..n.
        const val TYPE_INT_DEC: UByte = 0x10u
        // The 'data' is a raw integer value of the form 0xn..n.
        const val TYPE_INT_HEX: UByte = 0x11u
        // The 'data' is either 0 or 1, for input "false" or "true" respectively.
        const val TYPE_INT_BOOLEAN: UByte = 0x12u

        // Beginning of color integer flavors...
        const val TYPE_FIRST_COLOR_INT: UByte = 0x1cu

        // The 'data' is a raw integer value of the form #aarrggbb.
        const val TYPE_INT_COLOR_ARGB8: UByte = 0x1cu
        // The 'data' is a raw integer value of the form #rrggbb.
        const val TYPE_INT_COLOR_RGB8: UByte = 0x1du
        // The 'data' is a raw integer value of the form #argb.
        const val TYPE_INT_COLOR_ARGB4: UByte = 0x1eu
        // The 'data' is a raw integer value of the form #rgb.
        const val TYPE_INT_COLOR_RGB4: UByte = 0x1fu

// ...end of integer flavors.

        // ...end of integer flavors.
        const val TYPE_LAST_INT: UByte = 0x1fu


        // Structure of complex data values (TYPE_UNIT and TYPE_FRACTION)
// Where the unit type information is.  This gives us 16 possible
// types, as defined below.
        const val COMPLEX_UNIT_SHIFT = 0
        const val COMPLEX_UNIT_MASK = 0xf

        // TYPE_DIMENSION: Value is raw pixels.
        const val COMPLEX_UNIT_PX = 0
        // TYPE_DIMENSION: Value is Device Independent Pixels.
        const val COMPLEX_UNIT_DIP = 1
        // TYPE_DIMENSION: Value is a Scaled device independent Pixels.
        const val COMPLEX_UNIT_SP = 2
        // TYPE_DIMENSION: Value is in points.
        const val COMPLEX_UNIT_PT = 3
        // TYPE_DIMENSION: Value is in inches.
        const val COMPLEX_UNIT_IN = 4
        // TYPE_DIMENSION: Value is in millimeters.
        const val COMPLEX_UNIT_MM = 5

        // TYPE_FRACTION: A basic fraction of the overall size.
        const val COMPLEX_UNIT_FRACTION = 0
        // TYPE_FRACTION: A fraction of the parent size.
        const val COMPLEX_UNIT_FRACTION_PARENT = 1

        // Where the radix information is, telling where the decimal place
// appears in the mantissa.  This give us 4 possible fixed point
// representations as defined below.
        const val COMPLEX_RADIX_SHIFT = 4
        const val COMPLEX_RADIX_MASK = 0x3

        // The mantissa is an integral number -- i.e., 0xnnnnnn.0
        const val COMPLEX_RADIX_23p0 = 0
        // The mantissa magnitude is 16 bits -- i.e, 0xnnnn.nn
        const val COMPLEX_RADIX_16p7 = 1
        // The mantissa magnitude is 8 bits -- i.e, 0xnn.nnnn
        const val COMPLEX_RADIX_8p15 = 2
        // The mantissa magnitude is 0 bits -- i.e, 0x0.nnnnnn
        const val COMPLEX_RADIX_0p23 = 3

        // Where the actual value is.  This gives us 23 bits of
// precision.  The top bit is the sign.
        const val COMPLEX_MANTISSA_SHIFT = 8
        const val COMPLEX_MANTISSA_MASK = 0xffffff


        // Possible data values for TYPE_NULL.
// The value is not defined.
        const val DATA_NULL_UNDEFINED = 0u
        // The value is explicitly defined as empty.
        const val DATA_NULL_EMPTY = 1u
    }
}