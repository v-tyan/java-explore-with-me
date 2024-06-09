package dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDTO {
    private String app;
    private String uri;
    private Long hits;
}
