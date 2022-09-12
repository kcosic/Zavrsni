using Newtonsoft.Json;
using System;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class IssueDTO
    {
        [JsonProperty]
        public int Id { get; set; }
        [JsonProperty]
        public DateTime DateCreated { get; set; }
        [JsonProperty]
        public DateTime DateModified { get; set; }
        [JsonProperty]
        public DateTime? DateDeleted { get; set; }
        [JsonProperty]
        public bool Deleted { get; set; }
        [JsonProperty]
        public int ReviewId { get; set; }
        [JsonProperty]
        public DateTime DateOfSubmission { get; set; }
        [JsonProperty]
        public string Summary { get; set; }
        [JsonProperty]
        public RequestDTO Request { get; set; }
    }
}