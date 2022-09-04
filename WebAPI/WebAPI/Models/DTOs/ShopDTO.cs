using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class ShopDTO
    {
        public ShopDTO()
        {
            Appointments = new HashSet<AppointmentDTO>();
            RepairHistories = new HashSet<RepairHistoryDTO>();
            Requests = new HashSet<RequestDTO>();
            Reviews = new HashSet<ReviewDTO>();
            ChildShops = new HashSet<ShopDTO>();
            Tokens = new HashSet<TokenDTO>();
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
        public string LegalName { get; set; }
        [JsonProperty]
        public string ShortName { get; set; }
        [JsonProperty]
        public int LocationId { get; set; }
        [JsonProperty]
        public string Vat { get; set; }
        [JsonProperty]
        public string Email { get; set; }
        [JsonProperty]
        public string Password { get; set; }
        [JsonProperty]
        public int? ParentShopId { get; set; }

        [JsonProperty]
        public ICollection<AppointmentDTO> Appointments { get; set; }
        [JsonProperty]
        public LocationDTO Location { get; set; }
        [JsonProperty]
        public ICollection<RepairHistoryDTO> RepairHistories { get; set; }
        [JsonProperty]
        public ICollection<RequestDTO> Requests { get; set; }
        [JsonProperty]
        public ICollection<ReviewDTO> Reviews { get; set; }
        [JsonProperty]
        public virtual ICollection<ShopDTO> ChildShops { get; set; }
        [JsonProperty]
        public virtual ShopDTO ParentShop { get; set; }
        [JsonProperty]
        public virtual ICollection<TokenDTO> Tokens { get; set; }
    }
}