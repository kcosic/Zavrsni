using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class ReviewController : BaseController
    {
        public ReviewController() : base(nameof(ReviewController)) { }

        [HttpGet]
        [Route("api/Review/{id}")]
        public BaseResponse RetrieveReview(string id)
        {
            try
            {
                var review = Db.Reviews.Find(id);
                if (review == null || review.Deleted)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(review.ToDTO());
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Review")]
        public BaseResponse RetrieveReviews()
        {
            try
            {
                var review = Db.Reviews.Where(x => x.UserId == AuthUser.Id && !x.Deleted).ToList();
                if (review == null)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(Review.ToListDTO(review));
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/Review/{id}")]
        public BaseResponse DeleteReview(string id)
        {
            try
            {
                var review = Db.Reviews.Find(id);
                if (review == null || review.Deleted)
                {
                    throw new Exception("Record not found");
                }
                review.DateDeleted = DateTime.Now;
                review.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Review")]
        public BaseResponse CreateReview(ReviewDTO newReviewDTO)
        {
            try
            {
                if (newReviewDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newReview = new Review
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    UserId = newReviewDTO.UserId,
                    Comment = newReviewDTO.Comment,
                    Rating = newReviewDTO.Rating,
                    ShopId = newReviewDTO.ShopId,
                };

                Db.Reviews.Add(newReview);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Review/{id}")]
        public BaseResponse UpdateReview([FromUri] string id, [FromBody] ReviewDTO reviewDTO)
        {
            try
            {
                if (id == null || reviewDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var review = Db.Reviews.Find(id);
                if (review == null || review.Deleted)
                {
                    throw new Exception("Record not found");
                }

                review.DateModified = DateTime.Now;
                review.UserId = reviewDTO.UserId;
                review.Comment = reviewDTO.Comment;
                review.Rating = reviewDTO.Rating;
                review.ShopId = reviewDTO.ShopId;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}
