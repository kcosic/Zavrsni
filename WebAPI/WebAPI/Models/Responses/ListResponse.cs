using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.Responses
{
    public class ListResponse<T>
    { 
        public List<T> Data { get; set; }
    }
}