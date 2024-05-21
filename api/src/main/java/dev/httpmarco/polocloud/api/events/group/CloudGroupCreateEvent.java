package dev.httpmarco.polocloud.api.events.group;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CloudGroupCreateEvent implements GroupEvent {
    private final CloudGroup cloudGroup;
}
