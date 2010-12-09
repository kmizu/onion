/* ************************************************************** *
 *                                                                *
 * Copyright (c) 2005, Kota Mizushima, All rights reserved.       *
 *                                                                *
 *                                                                *
 * This software is distributed under the modified BSD License.   *
 * ************************************************************** */
package onion.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.MessageFormat;

import onion.compiler.util.*;


/**
 * @author Kota Mizushima
 * Date: 2005/04/08
 */
public class OnionCompiler {
  private final CompilerConfig config;
  
  public OnionCompiler(CompilerConfig config) {
    this.config = config;
  }
  
  public CompilerConfig getConfig() {
    return config;
  }
  
  public CompiledClass[] compile(String[] fileNames) {
    InputSource[] srcs = new InputSource[fileNames.length];
    for (int i = 0; i < srcs.length; i++) {
      srcs[i] = new FileInputSource(fileNames[i]);        
    }
    return compile(srcs);
  }
  
  public CompiledClass[] compile(InputSource[] srcs) {
    try {
      return (CompiledClass[])(new onion.compiler.Parsing(config).andThen(new Typing(config)).andThen(new Generating(config)).process(srcs)); 
    }catch(CompilationException ex){
      for(CompileError error:ex) printError(error);
      System.err.println(Messages.get("error.count", ex.size()));
      return null;
    }
  }
  
  private void printError(CompileError error) {
    Location location = error.getLocation();
    String sourceFile = error.getSourceFile();
    StringBuffer message = new StringBuffer();
    if(sourceFile == null){
      message.append(MessageFormat.format("{0}", error.getMessage()));
    }else{
      String line = null, lineNum = null;
      try {
        line = location != null ? getLine(sourceFile, location.line()) : "";
        lineNum = location != null ? Integer.toString(location.line()) : "";
      } catch (IOException e) {
        e.printStackTrace();
      }
      message.append(MessageFormat.format("{0}:{1}: {2}", sourceFile, lineNum, error.getMessage()));
      message.append(Systems.getLineSeparator());
      message.append("\t\t");
      message.append(line);
      message.append(Systems.getLineSeparator());
      message.append("\t\t");
      if(location != null){
        message.append(getCursor(location.column()));
      }
    }
    System.err.println(new String(message));
  }

  private String getCursor(int column) {
    return Strings.repeat(" ", column - 1) + "^";
  }
  
  private String getLine(String sourceFile, int lineNumber) throws IOException {
    BufferedReader reader = Inputs.newReader(sourceFile);
    try {
      int countLineNumber = 1;
      String line = null;
      while((line = reader.readLine()) != null){
        if(countLineNumber == lineNumber) break;
        countLineNumber++;
      }
      return line;
    } finally {
      reader.close();
    }
  }
}