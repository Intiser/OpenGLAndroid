/*
 * Copyright 2014-2016 Media for Mobile
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


package org.m4m.domain.pipeline;

import org.m4m.domain.ICommandHandler;
import org.m4m.domain.Render;

public class DrainRenderCommandHandler implements ICommandHandler {
    protected final Render render;

    public DrainRenderCommandHandler(Render render) {
        this.render = render;
    }

    @Override
    public void handle() {
        render.drain(0);
    }
}

