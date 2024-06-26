/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.api.packets.service;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class CloudServiceStateChangePacket extends Packet {

    //todo
    private UUID id;
    private ServiceState state;

    @Override
    public void read(PacketBuffer codecBuffer) {
        this.id = codecBuffer.readUniqueId();
        this.state = codecBuffer.readEnum(ServiceState.class);
    }

    @Override
    public void write(PacketBuffer codecBuffer) {
        codecBuffer.writeUniqueId(this.id);
        codecBuffer.writeEnum(this.state);
    }
}
