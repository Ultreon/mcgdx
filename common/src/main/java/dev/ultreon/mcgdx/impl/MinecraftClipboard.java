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

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;

/** Clipboard implementation for desktop that uses the system clipboard via GLFW.
 * @author mzechner */
public class MinecraftClipboard implements Clipboard {
	@Override
	public boolean hasContents () {
		String contents = getContents();
		return contents != null && !contents.isEmpty();
	}

	@Override
	public String getContents () {
		return GLFW.glfwGetClipboardString(((MinecraftGraphics)Gdx.graphics).getWindow().getWindowHandle());
	}

	@Override
	public void setContents (String content) {
		GLFW.glfwSetClipboardString(((MinecraftGraphics)Gdx.graphics).getWindow().getWindowHandle(), content);
	}
}
