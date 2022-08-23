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
        public int UserId { get; set; }
        [JsonProperty]
        public string TokenValue { get; set; }
        [JsonProperty]
        public UserDTO User { get; set; }
    }
}