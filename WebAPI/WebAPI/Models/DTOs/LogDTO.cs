using Newtonsoft.Json;
using System;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class LogDTO
    {
        [JsonProperty]
        public int Id { get; set; }
        [JsonProperty]
        public int UserId { get; set; }
        [JsonProperty]
        public DateTime Timestamp { get; set; }
        [JsonProperty]
        public string Application { get; set; }
        [JsonProperty]
        public string Source { get; set; }
        [JsonProperty]
        public string Severity { get; set; }
        [JsonProperty]
        public string Message { get; set; }
    }
}