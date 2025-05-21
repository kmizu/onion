/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2016-, Kota Mizushima, All rights reserved.  *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package onion.compiler.environment

import java.io._
import java.io.IOException
import java.io.InputStream
import org.apache.bcel.classfile._
import org.apache.bcel.classfile.ClassParser
import org.apache.bcel.classfile.JavaClass
import org.apache.bcel.util.Repository
import org.apache.bcel.util.ClassPath

/**
 * @author Kota Mizushima
 *
 */
class ClassFileTable(classPathString: String) {
  private val repository: Repository = org.apache.bcel.Repository.getRepository()
  private val classPath: ClassPath = new ClassPath(classPathString)

  /**
   * @param className fully qualified class name
   * @return
   */
  def load(className: String): JavaClass = {
    try {
      repository.loadClass(className)
    } catch {
      case e: ClassNotFoundException => add(className)
    }
  }

  def loadBytes(className: String): Array[Byte] = {
    try {
      val classFile: ClassPath.ClassFile = classPath.getClassFile(className)
      val in = classFile.getInputStream
      val out = new ByteArrayOutputStream()
      val buf = new Array[Byte](8192)
      var len = in.read(buf)
      while (len != -1) {
        out.write(buf, 0, len)
        len = in.read(buf)
      }
      in.close()
      out.toByteArray
    } catch {
      case _: IOException => null
    }
  }

  private def add(className: String): JavaClass = {
    try {
      val classFile: ClassPath.ClassFile = classPath.getClassFile(className)
      val input: InputStream = classFile.getInputStream
      val fileName: String = new File(classFile.getPath).getName
      val parser: ClassParser = new ClassParser(input, fileName)
      val javaClass: JavaClass = parser.parse
      input.close()
      repository.storeClass(javaClass)
      javaClass
    }
    catch {
      case e: IOException =>
        null
      case e: ClassFormatException =>
        null
    }
  }
}