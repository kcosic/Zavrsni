using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class ReviewDTO
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
        public int ShopId { get; set; }
        [JsonProperty]
        public string Comment { get; set; }
        [JsonProperty]
        public decimal Rating { get; set; }
        [JsonProperty]
        public ShopDTO Shop { get; set; }
        [JsonProperty]
        public UserDTO User { get; set; }
    }
}