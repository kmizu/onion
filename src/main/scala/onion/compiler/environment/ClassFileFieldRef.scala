/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005-2012, Kota Mizushima, All rights reserved.  *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package onion.compiler.environment

import onion.compiler.IRT

/**
 * @author Kota Mizushima
 *
 */
class ClassFileFieldRef(val modifier: Int, val affiliation: IRT.ClassTypeRef, val name: String, val `type`: IRT.TypeRef) extends IRT.FieldRef
