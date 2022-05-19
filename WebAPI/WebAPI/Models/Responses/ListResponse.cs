using System.Collections.Generic;

namespace WebAPI.Models.Responses
{
    public class ListResponse<T> : BaseResponse
    { 
        public List<T> Data { get; set; }
    }
}