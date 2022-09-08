using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class RequestDTO
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
        public decimal Price { get; set; }
        [JsonProperty]
        public DateTime EstimatedFinishDate { get; set; }
        [JsonProperty]
        public DateTime ActualFinishDate { get; set; }
        [JsonProperty]
        public decimal EstimatedPrice { get; set; }
        [JsonProperty]
        public decimal ActualPrice { get; set; }
        [JsonProperty]
        public string BillPicture { get; set; }
        [JsonProperty]
        public ShopDTO Shop { get; set; }
        [JsonProperty]
        public UserDTO User { get; set; }
    }
}