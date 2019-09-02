/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.cli;

import io.openvalidation.common.utils.ConsoleColors;
import java.util.Calendar;

public class Banner {
  public static String Text =
      "\n\n"
          + ConsoleColors.CYAN
          + "                .--://////////:--.\n"
          + "           `-:////////////////////:-`\n"
          + "           ////////////////////////:`\n"
          + "        /////////////////////////.\n"
          + "        ////////////////////////-      -:.\n"
          + "      ////////////////////////:`     .:///.\n"
          + "     +///////:---///////////:.      -//////`\n"
          + "    +/////:.`     -////////-      `:///////:    open\n"
          + "    /////-`        .//////.      -//////////    "
          + ConsoleColors.RESET
          + "VALIDATI"
          + ConsoleColors.CYAN
          + "O"
          + ConsoleColors.RESET
          + "N"
          + ConsoleColors.CYAN
          + "\n"
          + "    ///////-`       `///:`     `-///////////\n"
          + "    /////////.       ./-      `/////////////    "
          + ConsoleColors.RESET
          + "(c) "
          + Calendar.getInstance().get(Calendar.YEAR)
          + ConsoleColors.CYAN
          + "\n"
          + "    //////////-       `      .//////////////\n"
          + "    +//////////-            .//////////////:\n"
          + "     +//////////-          -///////////////`\n"
          + "      ///////////.        -///////////////.\n"
          + "       +//////////.    `-////////////////.\n"
          + "         /////////:``-/////////////////-`\n"
          + "           ////////////////////////////\n"
          + "              //////////////////////\n"
          + "                 //////////////\n"
          + ConsoleColors.RESET;
}
