namespace WebAPI.Models.Responses
{
    public class PaginatedResponse<T> : ListResponse<T>
    {
        public int Page { get; set; }
        public int PageSize { get; set; }
        public int Total { get; set; }
    }
}