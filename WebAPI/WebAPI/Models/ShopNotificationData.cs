using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models
{
    public class ShopNotificationData
    {
        public List<int> NewRequests { get; set; }

        public List<int> UpdatedRequests { get; set; }
    }
}