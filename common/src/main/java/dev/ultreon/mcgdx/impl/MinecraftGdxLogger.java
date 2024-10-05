/*
 * Copyright (c) 2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ultreon.mcgdx.impl;

import com.badlogic.gdx.ApplicationLogger;
import org.slf4j.LoggerFactory;

public class MinecraftGdxLogger implements ApplicationLogger {
    public MinecraftGdxLogger() {

    }

    @Override
    public void log(String tag, String message) {
        LoggerFactory.getLogger(tag).info(message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        LoggerFactory.getLogger(tag).info(message, exception);
    }

    @Override
    public void error(String tag, String message) {
        LoggerFactory.getLogger(tag).error(message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        LoggerFactory.getLogger(tag).error(message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        LoggerFactory.getLogger(tag).debug(message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        LoggerFactory.getLogger(tag).debug(message, exception);
    }
}
