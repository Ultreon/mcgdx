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

import java.awt.Desktop;
import java.net.URI;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.NetJavaImpl;
import com.badlogic.gdx.net.NetJavaServerSocketImpl;
import com.badlogic.gdx.net.NetJavaSocketImpl;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.SharedLibraryLoader;

/** LWJGL implementation of the {@link Net} API, it could be reused in other Desktop backends since it doesn't depend on LWJGL.
 * @author acoppes */
public class MinecraftNet implements Net {

	NetJavaImpl netJavaImpl;

	public MinecraftNet (MinecraftApplicationConfiguration configuration) {
		netJavaImpl = new NetJavaImpl(configuration.maxNetThreads);
	}

	@Override
	public void sendHttpRequest (HttpRequest httpRequest, HttpResponseListener httpResponseListener) {
		netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
	}

	@Override
	public void cancelHttpRequest (HttpRequest httpRequest) {
		netJavaImpl.cancelHttpRequest(httpRequest);
	}

	@Override
	public ServerSocket newServerSocket (Protocol protocol, String ipAddress, int port, ServerSocketHints hints) {
		return new NetJavaServerSocketImpl(protocol, ipAddress, port, hints);
	}

	@Override
	public ServerSocket newServerSocket (Protocol protocol, int port, ServerSocketHints hints) {
		return new NetJavaServerSocketImpl(protocol, port, hints);
	}

	@Override
	public Socket newClientSocket (Protocol protocol, String host, int port, SocketHints hints) {
		return new NetJavaSocketImpl(protocol, host, port, hints);
	}

	@Override
	public boolean openURI (String uri) {
		if (SharedLibraryLoader.isMac) {
			try {
				(new ProcessBuilder("open", (new URI(uri).toString()))).start();
				return true;
			} catch (Throwable t) {
				return false;
			}
		} else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(uri));
				return true;
			} catch (Throwable t) {
				return false;
			}
		} else if (SharedLibraryLoader.isLinux) {
			try {
				(new ProcessBuilder("xdg-open", (new URI(uri).toString()))).start();
				return true;
			} catch (Throwable t) {
				return false;
			}
		}
		return false;
	}
}
