using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class CarDTO
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
        public int UserId { get; set; }
        [JsonProperty]
        public string Manufacturer { get; set; }
        [JsonProperty]
        public string Model { get; set; }
        [JsonProperty]
        public int Year { get; set; }
        [JsonProperty]
        public decimal Odometer { get; set; }
      
        [JsonProperty]
        public ICollection<RequestDTO> Requests { get; set; }
        [JsonProperty]
        public UserDTO User { get; set; }
    }
}