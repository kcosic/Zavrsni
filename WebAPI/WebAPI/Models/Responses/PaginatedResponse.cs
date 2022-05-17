using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.Responses;

namespace WebAPI.Models.Bases
{
    public class PaginatedResponse<T> : ListResponse<T>
    {
        public int Page { get; set; }
        public int PageSize { get; set; }
        public int Total { get; set; }


    }
}