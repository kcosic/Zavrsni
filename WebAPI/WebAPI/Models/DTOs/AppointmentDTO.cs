using Newtonsoft.Json;
using System;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class AppointmentDTO
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
        public int ShopId { get; set; }
        [JsonProperty]
        public DateTime DateTimeStart { get; set; }        
        [JsonProperty]
        public DateTime DateTimeEnd { get; set; }

        [JsonProperty]
        public ShopDTO Shop { get; set; }
    }
}