using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class TokenDTO
    {
        [JsonProperty]
        public string TokenValue { get; set; }
        [JsonProperty]
        public int? UserId { get; set; }        
        [JsonProperty]
        public int? ShopId { get; set; }

        [JsonProperty]
        public UserDTO User { get; set; }
        [JsonProperty]
        public ShopDTO Shop { get; set; }
    }
}