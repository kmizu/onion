/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2016-, Kota Mizushima, All rights reserved.  *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package onion.compiler

import java.io.Reader
import onion.compiler.toolbox.Inputs

class FileInputSource(val name: String) extends InputSource {
  private lazy val reader: Reader = Inputs.newReader(name)

  def openReader: Reader = reader
}