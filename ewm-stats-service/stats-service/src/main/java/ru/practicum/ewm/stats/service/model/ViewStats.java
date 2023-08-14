package ru.practicum.ewm.stats.service.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
public class ViewStats {
    String app;
    String uri;
    Long hits;

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
