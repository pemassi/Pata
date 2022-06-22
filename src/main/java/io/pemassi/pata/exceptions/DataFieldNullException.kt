/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.exceptions

import kotlin.reflect.KProperty

data class DataFieldNullException(
    val property: KProperty<*>
): Exception("Data field '${property.name}' is null.")