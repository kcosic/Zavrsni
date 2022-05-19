namespace WebAPI.Models.Responses
{
    public class SingleResponse<T> : BaseResponse
    {
        public T Data{ get; set; }
    }
}