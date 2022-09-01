using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class LocationDTO
    {
        public LocationDTO()
        {
            Shops = new HashSet<ShopDTO>();
        }

        [JsonProperty]
        public int Id { get; set; }
        [JsonProperty]
        public DateTime DateCreated { get; set; }
        [JsonProperty]
        public DateTime DateModified { get; set; }
        [JsonProperty]
        public DateTime? DateDeleted { get; set; }
        [JsonProperty]
        public bool? Deleted { get; set; }
        [JsonProperty]
        public int? UserId { get; set; }
        [JsonProperty]
        public string Street { get; set; }
        [JsonProperty]
        public string StreetNumber { get; set; }
        [JsonProperty]
        public string City { get; set; }
        [JsonProperty]
        public string County { get; set; }
        [JsonProperty]
        public string Country { get; set; }
        [JsonProperty]
        public double? Longitude { get; set; }
        [JsonProperty]
        public double? Latitude { get; set; }
        [JsonProperty]
        public ICollection<ShopDTO> Shops { get; set; }
        [JsonProperty]
        public UserDTO User { get; set; }
    }
}