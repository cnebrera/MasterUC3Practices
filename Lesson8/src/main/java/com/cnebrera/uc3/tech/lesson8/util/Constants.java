package com.cnebrera.uc3.tech.lesson8.util;

import java.io.File;

/**
 * Constants Class
 * --------------------------------------
 *
 * @author Angel Iglesias Sanchez
 * --------------------------------------
 */
public final class Constants {
  public static final String ROOT = "Lesson8";
  /** Constant - Java folder */
  public static final String JAVA_FOLDER		      			  = ROOT + File.separator + "src" + File.separator + "main" + File.separator + "java";
  /**
   * Constant - Resources folder
   */
  public static final String RESOURCES_FOLDER = ROOT + File.separator + "src" + File.separator + "main" + File.separator + "resources";
  /**
   * Constant - Velocity template name
   */
  public static final String VELOCITY_TEMPLATE_NAME = Constants.RESOURCES_FOLDER + File.separator + "velocityClassExample.vm";
  /**
   * Constant - Velocity Class Generated - Package Name
   */
  public static final String VELOCITY_CLASS_GENERATED_PACKAGE = "com.cnebrera.uc3.tech.lesson8.velocity";
  /**
   * Constant - Velocity Class Generated - Class Name
   */
  public static final String VELOCITY_CLASS_GENERATED_CLASSNAME = "VelocityClassExample";

  /**
   * Private constructor
   */
  private Constants() {
    // Empty constructor
  }
}
