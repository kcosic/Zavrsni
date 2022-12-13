using Newtonsoft.Json;
using System;

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
        public int RequestId { get; set; }        
        [JsonProperty]
        public bool? Accepted { get; set; }
        [JsonProperty]
        public decimal Price { get; set; }
        [JsonProperty]
        public string Description{ get; set; }
        [JsonProperty]
        public RequestDTO Request { get; set; }        

    }
}