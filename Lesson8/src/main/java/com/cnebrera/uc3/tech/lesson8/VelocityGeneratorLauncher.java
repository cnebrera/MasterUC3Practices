package com.cnebrera.uc3.tech.lesson8;

import com.cnebrera.uc3.tech.lesson8.util.Constants;
import com.cnebrera.uc3.tech.lesson8.velocity.VelocityHandler;

import java.io.IOException;

/**
 * Launcher class - Velocity generator
 * --------------------------------------
 *
 * @author Angel Iglesias Sanchez
 * --------------------------------------
 */
public class VelocityGeneratorLauncher {
  /**
   * @throws IOException with an occurred exception
   */
  protected void generateClassfromVelocity() {
    // TODO 1
    // Create a new instance of the Velocity Handler
    final VelocityHandler velocityHandler = new VelocityHandler();

    // TODO 2
    // Generate the class
    velocityHandler.generateClassFromVelocityTemplate(Constants.VELOCITY_CLASS_GENERATED_PACKAGE, Constants.VELOCITY_CLASS_GENERATED_CLASSNAME);

  }

  /**
   * @param args with the input arguments
   * @throws IOException with an occurred exception
   */
  public static void main(final String[] args) {
    final VelocityGeneratorLauncher velocityGeneratorLauncher = new VelocityGeneratorLauncher();

    velocityGeneratorLauncher.generateClassfromVelocity();
  }

}
