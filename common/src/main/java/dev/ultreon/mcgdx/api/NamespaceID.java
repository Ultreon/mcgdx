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

package dev.ultreon.mcgdx.api;

import com.badlogic.gdx.utils.GdxRuntimeException;

public record NamespaceID(String domain, String namespace) {
    public static NamespaceID parse(String toParse) {
        String[] split = toParse.split(":");
        if (split.length == 0) {
            return new NamespaceID("minecraft", "");
        }
        if (split.length == 1) {
            for (char c : split[0].toCharArray()) {
                if (!(c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '/' || c == '.')) {
                    throw new GdxRuntimeException("Invalid ID namespace character: " + c);
                }
            }

            return new NamespaceID("minecraft", split[1]);
        }

        for (char c : split[0].toCharArray()) {
            if (!(c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.')) {
                throw new GdxRuntimeException("Invalid ID domain character: " + c);
            }
        }

        for (char c : split[1].toCharArray()) {
            if (!(c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '/' || c == '.')) {
                throw new GdxRuntimeException("Invalid ID namespace character: " + c);
            }
        }

        return new NamespaceID(split[0], split[1]);
    }
}
